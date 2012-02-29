(ns cljbin.core
  (:use (ciste [config :only [config load-config set-environment!]])
        somnium.congomongo)
  (:require (cljbin [http :as http])))

(defn split-mongo-url [url]
  "Parses mongodb url from heroku, eg. mongodb://user:pass@localhost:1234/db"
  (let [matcher (re-matcher #"^.*://(.*?):(.*?)@(.*?):(\d+)/(.*)$" url)]
    (when (.find matcher)
      (zipmap [:match :user :pass :host :port :db] (re-groups matcher)))))

(defn make-mongo-conn [config]
  (make-connection "cljbin"
                   :host (or (:host config) "127.0.0.1")
                   :port (if (:port config)
                           (Integer. (:port config))
                           27017)))

(defn start []
  (load-config)
  (set-environment! :development)
  (let [config (split-mongo-url (or (System/getenv "MONGOHQ_URL") ""))]
    (set-connection! (make-mongo-conn config))
    (if (and (:user config) (:pass config))
      (authenticate (:user config) (:pass config))))
  (let [port (Integer. (or (System/getenv "PORT") (config :http :port) 8082))]
    (println (format "Starting server on port: %d" port))
    (http/start port))
  @(promise))

(defn -main []
  (start))

