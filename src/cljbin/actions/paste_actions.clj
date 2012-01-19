(ns cljbin.actions.paste-actions
  (:use (ciste [core :only [defaction]]
               [config :only [definitializer]]))
  (:require [cljbin.model.paste :as paste]))

(defaction index
  []
  true
  )

(defaction show
  [id]
  (paste/fetch-by-id id))

(defaction put
  [params]
  (paste/create! params))

(defaction delete
  [id]
  true
  )

(definitializer
  (doseq [namespace ['cljbin.views.paste-views
                     'cljbin.filters.paste-filters
                     'cljbin.sections.paste-sections]]
    (require namespace)))

