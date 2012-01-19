(ns cljbin.filters.paste-filters
  (:use (ciste [filters :only [deffilter]])
        cljbin.actions.paste-actions))

(deffilter #'index :http
  [action request]
  (action))

(deffilter #'show :http
  [action request]
  (-> request :params :id action))

(deffilter #'put :http
  [action request]
  (-> request :params action))

(deffilter #'delete :http
  [action request]
  (-> request :params :id action))

