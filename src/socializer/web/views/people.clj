(ns socializer.web.views.people
  (:use ring.util.response
        [socializer.web.views.index :only [app-template]])
  (:require [net.cgrand.enlive-html :as html]))

(html/defsnippet people-form "templates/people-form.html"
  [:#content]
  [people]
  [:#people] (html/content (clojure.string/join "\n"
                             (sort people))))

(defn parse-set-of-people [params]
  (let [{people :people} params
        people (clojure.string/trim people)
        people (clojure.string/split people #"\n")
        people (map clojure.string/trim people)
        people (filter (complement empty?) people)
        people (set people)]
    people))

(defn store-people [session params]
  (let [people (parse-set-of-people params)]
    (-> (redirect-after-post "/people")
        (assoc :session {:people people}))))

(defn list-people [session]
  (response (app-template {:title "People"
                           :active-nav "people"
                           :content (people-form (:people session))})))

