(ns socializer.web.views.events
  (:use [socializer.web.util :only [response]]
        [ring.util.response :only [redirect-after-post]]
        [socializer.web.views.people :only [parse-people]])
  (:require [clojure.string :as string]
            [socializer.web.views.template :as template]
            [net.cgrand.enlive-html :as html]))

(defn- split-words
  [words]
  (clojure.string/split words #"\s+"))

(defn- parse-table-line
  [line]
  (let [name-shape-nr (split-words line)
        nr-chairs (last name-shape-nr)
        name-shape (drop-last name-shape-nr)
        shape (last name-shape)
        name-words (drop-last name-shape)
        table-name (clojure.string/join " " name-words)]
    ; TODO Validate shape keyword
    {table-name {:shape (keyword shape)
                 :nr-chairs (Integer. nr-chairs)}}))

(defn parse-tables
  [tables]
  (->> tables
       string/split-lines
       (map string/trim)
       (filter (complement empty?))
       (map parse-table-line)
       (apply merge)))

(defn unparse-tables
  [tables]
  (clojure.string/join "\n"
                       (sort (map #(str %
                                        " "
                                        (name (:shape (get tables %)))
                                        " "
                                        (:nr-chairs (get tables %)))
                                  (keys tables)))))

(html/defsnippet form-snippet "templates/event-form.html"
  [:#content]
  [event]

  [:#event-name] (html/set-attr :value (:event-name event))

  [:#date] (html/set-attr :value (:date event))

  [:#tables] (html/content (unparse-tables (:tables event)))

  [:#people] (html/content
               (clojure.string/join "\n"
                                    (sort (:participants event))))
  )

(defn ->form
  ([session]
   (response (template/base {:session session
                             :title "Add Event"
                             :active-nav "events"
                             :content (form-snippet {})})))
  ([session event-key]
   (response (template/base {:session session
                             :title "Edit Event"
                             :active-nav "events"
                             :content (form-snippet (get (:events session)
                                                         event-key))}))))

(defn ->store
  [session params]
  (let [event-key (str (:date params) " " (:event-name params))
        event {:event-name (:event-name params)
               :date (:date params)
               :tables (parse-tables (:tables params))
               :participants (parse-people params)}]
    (-> (redirect-after-post "/events")
      (assoc :session session)
      (assoc-in [:session :events event-key] event))))
