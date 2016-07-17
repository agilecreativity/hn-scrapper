(ns clj_scrapper.core
  (:require [clojure.pprint :as pp]
            [reaver :refer [parse extract-from text attr]])
  (:gen-class))

(def hacker-news (slurp "https://news.ycombinator.com"))

(defn extract-data
  "Extract some data from hacker-news"
  []
  (extract-from (parse hacker-news) ".itemlist .athing"
                [:headline :url]
                ".title > a" text
                ".title > a" (attr :href)))

(defn extract-field
  "Extract the given field from a given content map"
  [field content]
  (map field content))

(defn -main [& args]
  (let [content (extract-data)]
    (do
      ;; Let's see the sample content
      (pp/pprint (first content))

      ;; And get just the field we need
      (pp/pprint (map #(str "[" (:headline %) "](" (:url %) "]") content))
      ;(pp/pprint (extract-field :url content))
      )))
