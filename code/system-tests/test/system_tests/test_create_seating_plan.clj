(ns system-tests.test-create-seating-plan
  (:require [clj-webdriver.taxi :as taxi]
            [clojure.test :refer :all]))

(deftest test-single
  (testing "Selenium WebDriver"
    (taxi/set-driver! {:browser :phantomjs})
    (taxi/to "http://www.example.com/")
    (is (.contains (taxi/text "body") "used for illustrative examples"))
    (taxi/quit)))
