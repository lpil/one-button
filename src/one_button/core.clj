(ns one-button.core
  (:require [quil.core :as q]
            [quil.middleware]))

(def frames-per-tick 50)

(def tree
  {:one { }
   :two { }
   :three { }
   })

; (def width  810)
; (def height 540)
(def width 1080)
(def height 720)

(defn inc-tick [state]
  (let [size (count (keys tree))
        tick (-> state :tick inc (mod size))]
    (assoc state :tick tick)))

(defn setup []
  {:path []
   :tick 0
   }
  )

(defn step [state]
  (if (= (mod (q/frame-count) frames-per-tick) 0)
    (inc-tick state)
    state))

(defn draw [state]
  (q/background 0)
  (q/stroke 255)
  (q/text-size 100)
  (q/fill 255)
  (q/text-align :center :center)
  (q/translate (* 0.5 (q/width)) (* 0.5 (q/height)))


  (let [choices (keys tree)
        current (nth choices (:tick state))]
    (q/text (name current) 0 0)
    )
  )

(defn run []
  (q/defsketch example
    :title "Art!"
    :middleware [quil.middleware/fun-mode]
    :settings #(q/smooth 2)
    :setup setup
    :update step
    :draw draw
    :size [800 800]
    ))
(run)
