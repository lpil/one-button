(ns one-button.core
  (:require [quil.core :as q]
            [quil.middleware]))

(def frames-per-tick 30)

(def tree
  {:one   {:one-one {}
           :one-two {}}
   :two   {:two-one {}
           :two-two {}}
   :three {:three-one {}
           :three-two {}}})

; (def width  810)
; (def height 540)
(def width 1080)
(def height 720)

(defn inc-tick [state]
  (let [max-tick (count (:choices state))
        tick     (-> state :tick inc (mod max-tick))]
    (assoc state :tick tick)))

(defn current-choice [state]
    (->> (:tick state) (nth (:choices state))))

(defn setup []
  {:path []
   :tick 0
   :choices (conj (keys tree) :back)})

(defn climb-tree [state]
  (let [picked      (current-choice state)
        new-path    (conj (:path state) picked)
        next-level  (get-in tree new-path)
        new-choices (keys next-level)
        with-back   (conj new-choices :back)]
    (assoc state
           :path new-path
           :choices with-back
           :tick 0)))

(defn key-pressed [state event]
  (if (= :back (current-choice state))
    state ; TODO: go back
    (climb-tree state)
    ))

(key-pressed (setup) 1)

(def round (assoc (setup) :tick 1))
(key-pressed round 1)
(get-in tree [:one])

(defn step [state]
  (if (= (mod (q/frame-count) frames-per-tick) 0)
    (inc-tick state)
    state))

(current-choice (setup))

(defn draw [state]
  (q/background 0)
  (q/stroke 255)
  (q/fill 255)
  (q/text-align :center :center)
  (q/translate (* 0.5 (q/width)) (* 0.5 (q/height)))

  (let [display (->> (:choices state) (map name) (clojure.string/join " "))
        current (current-choice state)]

    (q/text-size 20)
    (q/text (str state) 0 -200)

    (q/text-size 100)
    (q/text (name current) 0 0)
    (q/text-size 30)
    (q/text display 0 200)
    ))

(defn run []
  (q/defsketch example
    :title "Art!"
    :middleware [quil.middleware/fun-mode]
    :settings #(q/smooth 2)
    :setup setup
    :update step
    :key-pressed key-pressed
    :draw draw
    :size [800 800]
    ))
(run)
