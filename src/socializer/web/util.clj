(ns socializer.web.util
  (:require ring.util.response))

(defn response
  [body]
  (-> (ring.util.response/response body)
      (ring.util.response/content-type "text/html")
      (ring.util.response/charset "utf-8")))
