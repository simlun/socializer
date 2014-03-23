(ns socializer.web.views.template-test
  (:use midje.sweet
        ring.mock.request
        [clojure.string :only [join trim]])
  (:require [net.cgrand.enlive-html :as html]
            [socializer.web.views.template :as sut])
  (:import (java.io StringReader)))

(defn nodes
  [response]
  (-> response
      join
      StringReader.
      html/html-resource))

(defn select-texts
  [html-str selectors]
  (map trim
       (html/texts
         (html/select (nodes html-str)
                      selectors))))

(fact "The title can be set"
  (select-texts
    (sut/base {:title "Testing"})
    [:title])
  => (just ["Socializer - Testing"]))

(facts "Nav bar items can be activated"
  (select-texts
    (sut/base {:active-nav "people"})
    [:.active])
  => (just ["People"]))
