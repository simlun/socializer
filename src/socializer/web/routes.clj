(ns socializer.web.routes
  (:use compojure.core)
  (:require compojure.handler
            compojure.route
            [socializer.web.views.index :as index]
            [socializer.web.views.people :as people]
            [socializer.web.views.groups :as groups]
            [socializer.web.views.events :as events]
            [socializer.web.views.seating :as seating]
            [socializer.web.views.data :as data]))

(defroutes my-routes
  (GET "/"
       {session :session}
       (index/index session))

  (GET "/people"
       {session :session}
       (people/form session))

  (POST "/people"
        {session :session
         params :params}
        (people/store session params))

  (GET "/groups"
       {session :session}
       (groups/form session))

  (GET "/groups/:group-name"
       {session :session
        {group-name :group-name} :params}
       (groups/form session group-name))

  (POST "/groups"
        {session :session
         params :params}
        (groups/store session params))

  (GET "/events"
       {session :session}
       (events/form session))

  (GET "/events/:event-key"
       {session :session
        {event-key :event-key} :params}
       (events/form session event-key))

  (POST "/events"
        {session :session
         params :params}
        (events/store session params))

  (GET "/seating-plan"
       {session :session}
       (seating/plan session))

  (GET "/data"
       {session :session}
       (data/form session))

  (compojure.route/resources "/")
  (compojure.route/not-found "Page not found"))

(def handler (-> my-routes
                 compojure.handler/site))
