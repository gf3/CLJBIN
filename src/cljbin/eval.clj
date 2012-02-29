(ns cljbin.eval
  (:use [clojail.testers :only [secure-tester-without-def]]
        [clojail.core :only [sandbox]]
        [clojure.stacktrace :only [root-cause]])
  (:require [clojure.string :as string :only [replace trim]])
  (:import java.io.StringWriter
           java.util.concurrent.TimeoutException))

; A gracious thank you to amalloy in #clojure for the following fn
(defn- read-string-safely [s]
  (binding [*read-eval* false]
    (with-in-str s
      (let [end (Object.)]
        (doall (take-while (complement #{end})
                           (repeatedly #(read *in* false end))))))))

(defn- make-sandbox []
  (sandbox (into secure-tester-without-def #{'sandbox.core})
           :timeout 5000
           :init '(do
                    (use 'clojure.repl 'clojure.set 'clojure.test)
                    (require '[clojure.string :as string]))))

(defn- execute [sb writer expr]
  (try
    (sb expr {#'*out* writer})
    (catch TimeoutException _ "Execution timed out!")
    (catch Exception e (-> e root-cause str))))

(defn run [code-string]
  (let [sb (make-sandbox)]
    (for [expr (read-string-safely code-string)
          :let [writer (StringWriter.)
                res (execute sb writer expr)
                output (str writer)]]
      (do
        (.close writer)
        (string/trim (str output res)))))) 

