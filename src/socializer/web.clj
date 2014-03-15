(ns socializer.web
  (:use compojure.core)
  (:require compojure.handler
            compojure.route
            [socializer.web.views :as views]))

(defroutes my-routes
  (GET "/" [] (views/index))
  (compojure.route/resources "/")
  (compojure.route/not-found "Page not found"))

(def handler (compojure.handler/site my-routes))
