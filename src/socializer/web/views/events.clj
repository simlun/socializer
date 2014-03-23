(ns socializer.web.views.events
  (:use ring.util.response
        [socializer.web.views.people :only [parse-set-of-people]])
  (:require [socializer.web.views.template :as template]
            [net.cgrand.enlive-html :as html]))

(html/defsnippet form-snippet "templates/event-form.html"
  [:#content]
  [event-name event]

  [:#event-name] (html/set-attr :value event-name)

  [:#people] (html/content (clojure.string/join "\n" (sort (:participants event)))))

(defn ->form
  ([session]
   (response (template/base {:session session
                             :title "Add Event"
                             :active-nav "events"
                             :content (form-snippet "" {})}))))
