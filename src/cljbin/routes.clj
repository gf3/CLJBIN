(ns cljbin.routes
  (:use (ciste [debug :only [spy]]
               [routes :only [make-matchers resolve-routes]])
        ciste.formats.default
        cljbin.views)
  (:require (ciste [middleware :as middleware]
                   [predicates :as pred])
            (compojure [core :as compojure]
                       [handler :as handler]
                       [route :as route])
            (cljbin.actions [paste-actions :as paste])
            (ring.middleware [anti-forgery :as anti-forgery]
                             [cssgen :as cssgen]
                             [file :as file]
                             [file-info :as file-info]
                             [reload-modified :as reload]
                             [stacktrace :as stacktrace])
            (ring.util [response :as response])))

(def main-routes
  (make-matchers
   [
    [[:get    "/"]            #'paste/index]
    [[:get    "/paste/:id"]   #'paste/show]
    [[:put    "/paste"]       #'paste/put]
    [[:delete "/paste/:id"]   #'paste/delete]
    ]))


(def http-predicates
  [#'pred/http-serialization?
   [#'pred/request-method-matches?
    #'pred/path-matches?]])

(compojure/defroutes all-routes
  (resolve-routes [http-predicates] main-routes))

(def app
  (-> all-routes
    (file/wrap-file "resources/public/")
    file-info/wrap-file-info
    (reload/wrap-reload-modified ["src"])
    ; anti-forgery/wrap-anti-forgery
    ; (cssgen/wrap-cssgen cssgen/css-req?)
    handler/site
    middleware/wrap-http-serialization
    stacktrace/wrap-stacktrace))

