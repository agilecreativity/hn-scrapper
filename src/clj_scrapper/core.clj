(ns clj_scrapper.core
  (:require [clojure.java.io :as io]
            [clojure.pprint :as pp]
            [reaver :refer [parse extract-from text attr]])
  (:gen-class))

(defn- hacker-news-url
  "Return URL from hacker-news for a given page"
  [page-number]
  (str "https://news.ycombinator.com/news?p=" page-number))

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
  (map #(str "[" (:headline %) "](" (:url %) ")") content))

(defn -main [& args]
  (with-open [w (io/writer "/home/bchoomnuan/Desktop/hacker-news.md")]
    (.write w "### Hacker News Index - 10 pages")
    (.newLine w)
    ;; Note: Hacker News only show the last 20 pages
    (doseq [n (range 10)]
      (let [content (extract-data (inc n))]
        ;; And get just the field we need
        (doseq [line (markdown-links content)]
          ;(pp/pprint line)
          ;; Print this as a list
          (.write w (str " - " line))
          (.newLine w))))))
