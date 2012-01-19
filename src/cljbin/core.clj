(ns cljbin.core
  (:use (ciste [config :only [config load-config set-environment!]])
        somnium.congomongo)
  (:require (cljbin [http :as http])))

(def mongo-conn 
  (make-connection "cljbin"
                   :host "127.0.0.1"
                   :port 27017))

(defn start []
  (load-config)
  (set-environment! :development)
  (set-connection! mongo-conn)
  (let [port (or (config :http :port) 8082)]
    (println (format "Starting server on port: %d" port))
    (http/start port))
  @(promise))

(defn -main []
  (start))

