(ns socializer.web
  (:use compojure.core)
  (:require compojure.handler
            compojure.route
            [socializer.web.views :as views]))

(defroutes my-routes
  (GET "/" [] (views/index))

  (POST "/people"
        {session :session
         params :params}
        (views/store-people session params))

  (GET "/people" {session :session} (views/list-people session))

  (compojure.route/resources "/")
  (compojure.route/not-found "Page not found"))

(def handler (compojure.handler/site my-routes))
