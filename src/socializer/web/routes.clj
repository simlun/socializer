(ns socializer.web.routes
  (:use compojure.core)
  (:require compojure.handler
            compojure.route
            [socializer.web.views.index :as index]
            [socializer.web.views.people :as people]
            [socializer.web.views.groups :as groups]))

(defroutes my-routes
  (GET "/" [] (index/index))

  (POST "/people"
        {session :session
         params :params}
        (people/store-people session params))

  (GET "/people" {session :session} (people/list-people session))

  (GET "/groups" {session :session} (groups/form-page session))

  (compojure.route/resources "/")
  (compojure.route/not-found "Page not found"))

(def handler (compojure.handler/site my-routes))
