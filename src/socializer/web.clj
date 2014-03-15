(ns socializer.web
  (:use compojure.core)
  (:require [compojure.handler]
            [socializer.web.views :as views]))

(defroutes my-routes
  (GET "/" [] (views/index))
  )

(def handler (compojure.handler/site my-routes))
