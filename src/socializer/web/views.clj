(ns socializer.web.views
  (:use ring.util.response)
  (:require [net.cgrand.enlive-html :as html]))

(html/deftemplate app-template "templates/app.html"
  [title active-nav content]
  [:head :title] (html/append title)
  [:.nav :.active] (html/remove-class "active")
  [(keyword (str "#nav-" active-nav))] (html/add-class "active")
  [:#content] (html/content content))

(html/defsnippet people-form "templates/people-form.html"
  [:body]
  [people]
  [:#people] (html/content (str people)))

(defn index [] (response (app-template "Home" "index" "TBD")))

(defn parse-set-of-people [params]
  (let [{people :people} params
        people (clojure.string/split people #"\n")
        people (set people)]
    people))

(defn store-people [session params]
  (let [people (parse-set-of-people params)]
    (-> (redirect-after-post "/people")
        (assoc :session {:people people}))))

(defn list-people [session]
  (response (app-template "People"
                          "people"
                          (people-form (:people session)))))
