(ns socializer.web.routes
  (:use compojure.core)
  (:require compojure.handler
            compojure.route
            [socializer.web.views.index :as index]
            [socializer.web.views.people :as people]
            [socializer.web.views.groups :as groups]
            [socializer.web.views.events :as events]
            [socializer.web.views.data :as data]))

(defroutes my-routes
  (GET "/"
       {session :session}
       (index/->index session))

  (GET "/people"
       {session :session}
       (people/->form session))

  (POST "/people"
        {session :session
         params :params}
        (people/->store session params))

  (GET "/groups"
       {session :session}
       (groups/->form session))

  (GET "/groups/:group-name"
       {session :session
        {group-name :group-name} :params}
       (groups/->form session group-name))

  (POST "/groups"
        {session :session
         params :params}
        (groups/->store session params))

  (GET "/events"
       {session :session}
       (events/->form session))

  (GET "/data"
       {session :session}
       (data/->form session))

  (compojure.route/resources "/")
  (compojure.route/not-found "Page not found"))

(def handler (compojure.handler/site my-routes))
