(ns socializer.web.views.groups
  (:use ring.util.response
        [socializer.web.views.index :only [app-template]]
        [socializer.web.views.people :only [parse-set-of-people]])
  (:require [net.cgrand.enlive-html :as html]))


(html/defsnippet form-snippet "templates/group-form.html"
  [:#content]
  [group-name group]

  [:#group-name] (html/set-attr :value group-name)

  [:select (html/attr= :value (:intention group))]
  (html/set-attr :selected "selected")

  [:#people] (html/content (clojure.string/join "\n" (sort (:members group)))))


(defn ->form
  ([session]
   (response (app-template {:session session
                            :title "Add Group"
                            :active-nav "groups"
                            :content (form-snippet "" {})})))
  ([session group-name]
   (response (app-template {:session session
                            :title "Edit Group"
                            :active-nav "groups"
                            :content (form-snippet
                                       group-name
                                       (get (:groups session) group-name))}))))


(defn ->store [session params]
  (let [group-name (:group-name params)
        group {:intention (:intention params)
               :members (parse-set-of-people params)}]
    (-> (redirect-after-post "/groups")
        (assoc :session session)
        (assoc-in [:session :groups group-name] group))))
