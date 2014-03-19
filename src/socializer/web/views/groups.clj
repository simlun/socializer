(ns socializer.web.views.groups
  (:use ring.util.response
        [socializer.web.views.index :only [app-template]])
  (:require [net.cgrand.enlive-html :as html]))

(html/defsnippet form-snippet "templates/group-form.html"
  [:#content]
  [members]
  [:#members] (html/content (clojure.string/join "\n"
                              (sort members))))

(defn ->form [session]
  (response (app-template {:title "Add Group"
                           :active-nav "groups"
                           :content (form-snippet (:members session))})))
