(ns cljbin.http
  (:use (cljbin [routes :only [app]])
        ; (aleph [http :only [start-http-server]])))
        (ring.adapter [jetty :only [run-jetty]])))

; (defn start
  ; ([] (start 8082))
  ; ([port]
     ; (start-http-server
        ; #'app
        ; {:port port
         ; :websocket true
         ; :cljsc {:optimizations :simple}
         ; :join? false})))

(defn start
  ([] (start 8082))
  ([port]
   (run-jetty #'app {:join? false
                     :port port})))

