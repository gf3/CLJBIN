(ns cljbin.eval
  (:use [clojail.testers :only [secure-tester-without-def]]
        [clojail.core :only [sandbox]]
        [clojure.repl :only [doc] :rename {doc repl-doc}]
        [clojure.stacktrace :only [root-cause]])
  (:require [clojure.string :as string :only [replace trim]])
  (:import java.io.StringWriter
           java.util.concurrent.TimeoutException))

(defn- safe-read [s]
  (binding [*read-eval* false]
    (read-string s)))

; A gracious thank you to amalloy on #clojure for the following fn
(defn read-string-safely [s]
  (binding [*read-eval* false]
    (with-in-str s
      (let [end (Object.)]
        (doall (take-while (complement #{end})
                           (repeatedly #(read *in* false end))))))))

(defn- make-sandbox []
  (sandbox (into secure-tester-without-def #{'sandbox.core})
           :timeout 5000
           :init '(do
                    (use '[clojure.repl :rename {doc repl-doc}] 'clojure.set 'clojure.test)
                    (require '[clojure.string :as string])
                    (defmacro doc [form]
                      `(when-let [output# (with-out-str (repl-doc ~form))]
                         (-> output# (string/replace "-" "")
                                     (string/replace "\n" " ")
                                     (string/replace #"\s+" " ")
                                     string/trim))))))

(defn run [code-string]
  (try
    (let [sb (make-sandbox)]
      (for [expr (read-string-safely code-string)
            :let [writer (StringWriter.)
                  res (sb expr {#'*out* writer})
                  output (str writer)]]
        (do
          (.close writer)
          (string/trim (str output res)))))
    (catch TimeoutException _ "Execution timed out!")
    (catch Exception e (-> e root-cause str))))


