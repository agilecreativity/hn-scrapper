(ns clj_scrapper.core
  (:require [clojure.pprint :as pp]
            [reaver :refer [parse extract-from text attr]])
  (:gen-class))

(defn- hacker-news-url
  "Return URL from hacker-news for a given page"
  [page-number]
  (str "https://news.ycombinator.com/news?p=" page-number))

;(def hacker-news (slurp "https://news.ycombinator.com"))
(defn- hacker-news
  "Read the content of hacker-news for a given page"
  [page-number]
  (slurp (hacker-news-url page-number)))

(defn extract-data
  "Extract some data from hacker-news"
  [page-number]
  (extract-from (parse (hacker-news page-number)) ".itemlist .athing"
                [:headline :url]
                ".title > a" text
                ".title > a" (attr :href)))

(defn markdown-links
  "Create basic markdown link from content map"
  [content]
  (map #(str "[" (:headline %) "](" (:url %) "]") content))

(defn -main [& args]
  ;; Note: Hacker News only show the last 20 pages
  (dotimes [n 20]
    (let [content (extract-data (inc n))]
      (do
        ;; And get just the field we need
        (pp/pprint (markdown-links content))))))
