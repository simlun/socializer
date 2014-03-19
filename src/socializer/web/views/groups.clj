(ns socializer.web.views.groups
  (:use ring.util.response
        [socializer.web.views.index :only [app-template]])
  (:require [net.cgrand.enlive-html :as html]))

(html/defsnippet group-form "templates/group-form.html"
  [:#content]
  [members]
  [:#members] (html/content (clojure.string/join "\n"
                              (sort members))))

(defn form-page [session]
  (response (app-template {:title "Add Group"
                           :active-nav "groups" ; FIXME
                           :content (group-form (:members session))})))
