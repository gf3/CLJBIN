(defproject cljbin "1.0.0-SNAPSHOT"
  :description "A clojure pastebin. And then some."
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [aleph "0.2.1-SNAPSHOT"]
                 [ciste "0.2.0-SNAPSHOT"]
                 [clj-time "0.3.4"]
                 [clojail "0.5.1"]
                 [com.evocomputing/colors "1.0.0-SNAPSHOT"]
                 [compojure "1.0.0"]
                 [congomongo "0.1.7"]
                 [cssgen "0.3.0-SNAPSHOT"]
                 [hiccup "0.3.8"]
                 [potemkin "0.1.1"]
                 [ring-anti-forgery "0.1.1"]
                 [ring-cssgen "0.0.1-SNAPSHOT"]
                 [ring-reload-modified "0.1.1"]
                 [ring/ring-core "1.0.1"]
                 [ring/ring-devel "1.0.1"]
                 [ring/ring-jetty-adapter "1.0.1"]]
  :main cljbin.core
  :warn-on-reflection false
  :jvm-opts ["-server"
             "-XX:MaxPermSize=1024m"
             "-Djava.security.policy=example.policy"])

