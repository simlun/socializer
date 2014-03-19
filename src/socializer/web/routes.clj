(ns socializer.web.routes
  (:use compojure.core)
  (:require compojure.handler
            compojure.route
            [socializer.web.views.index :as index]
            [socializer.web.views.people :as people]
            [socializer.web.views.groups :as groups]))

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

  (compojure.route/resources "/")
  (compojure.route/not-found "Page not found"))

(def handler (compojure.handler/site my-routes))
