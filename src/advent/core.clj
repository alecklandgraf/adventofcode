; refresh with (use 'advent.core :reload-all)
(ns advent.core)

(import 'java.security.MessageDigest
        'java.math.BigInteger)

; advent #4
(defn md5 [s]
  (let [algorithm (MessageDigest/getInstance "MD5")
        size (* 2 (.getDigestLength algorithm))
        raw (.digest algorithm (.getBytes s))
        sig (.toString (BigInteger. 1 raw) 16)
        padding (apply str (repeat (- size (count sig)) "0"))]
    (str padding sig)))

(def my-key "bgvyzdsv")

(defn match?
    [result]
    (let [compt-test "000000"]
        (.startsWith result compt-test)))

(defn big-mama-jama
    "tests to see if hash result matching compt-test"
    [seed]
    (loop [x seed]
        (let [the-hash (md5 (str my-key x))]
            (if (match? the-hash)
                x
                (recur (inc x))))))

; advent #21 (unfinshed and not working)
(def boss {:hit-points 100 :damage 8 :armor 2})

(def weapon-store {[4 0] 8
                   [5 0] 10
                   [6 0] 25
                   [7 0] 40
                   [8 0] 74})

(def armor-store {[0 1] 13
                  [0 2] 31
                  [0 3] 53
                  [0 4] 75
                  [0 5] 102})

(def ring-store {[1 0] 25
                 [2 0] 50
                 [3 0] 100
                 [0 1] 20
                 [0 2] 40
                 [0 3] 80})

(defn minimum-stats
  [boss]
  (loop [player-defense (dec (:damage boss))
         stats []]
    ; need a special case for the 1-damage case (always buy the cheap weapon)
    (let [player-attack (+ (:armor boss) (:damage boss) (- player-defense))]
      (if (>= player-defense 0)
        (recur (dec player-defense) (conj stats [player-attack player-defense]))
        stats))))

(def ways-to-get-stats {[0 0] 0
                        [0 1] 13})

; day 3
(def day-3-input "/Users/alandgraf/code/advent/src/advent/day3.txt")

(defn update-position
  "updates the position of x and y based on charater from input c,
  returns new position as vector [new-x new-y]
  e.g. [1 5] and > will return [2 5] while [1 5] and v will return [1 4]"
  [[x y] c]
  (let [c (str c)]
    (case c
      "^" [x (inc y)]
      "v" [x (dec y)]
      ">" [(inc x) y]
      "<" [(dec x) y])))


(defn process-directions [directions]
  (loop [houses #{[0 0]}
         cur-house [0 0]
         directions (seq directions)]
    (if-not directions
      houses
      (let [c (first directions)
            new-pos (update-position cur-house c)]
        (recur (conj houses new-pos)
               new-pos
               (next directions))))))

(defn process-directions-v2 [directions]
  (reduce
    (fn [acc next-direction]
      (let [houses (:houses acc)
            last-house (:last-house acc)
            new-pos (update-position last-house next-direction)]
        {:houses (conj houses new-pos)
         :last-house new-pos}))
    {:houses #{[0 0]}
     :last-house [0 0]}
    directions))

(defn count-houses [directions]
  (count (process-directions directions)))

(defn count-houses-v2 [directions]
  (count (:houses (process-directions directions))))

; day 3 part 2
; my version of clojure doesn't have union?

;   Copyright (c) Rich Hickey. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(defn- bubble-max-key [k coll]
  "Move a maximal element of coll according to fn k (which returns a number)
   to the front of coll."
  (let [max (apply max-key k coll)]
    (cons max (remove #(identical? max %) coll))))

(defn union
  "Return a set that is the union of the input sets"
  {:added "1.0"}
  ([] #{})
  ([s1] s1)
  ([s1 s2]
     (if (< (count s1) (count s2))
       (reduce conj s2 s1)
       (reduce conj s1 s2)))
  ([s1 s2 & sets]
     (let [bubbled-sets (bubble-max-key count (conj sets s2 s1))]
       (reduce into (first bubbled-sets) (rest bubbled-sets)))))


; my code

(defn process-directions-robo [directions]
  (loop [houses #{[0 0]}
         cur-house [0 0]
         directions (seq directions)]
    (if-not directions
      houses
      (let [c (str (first (first directions)))  ; partition makes a lazy sequence so ">^" becomes ((\>) (\^))
            new-pos (update-position cur-house c)]
        (recur (conj houses new-pos)
               new-pos
               (next directions))))))

(defn robo-santa
  "usage: (robo-santa (slurp my-file))"
  [directions]
  (let [santas-directions (partition 1 2 directions)
        robos-directions (partition 1 2 (rest directions))
        santas-houses (process-directions-robo santas-directions)
        robos-houses (process-directions-robo robos-directions)
        houses-seen (union santas-houses robos-houses)]
    (count houses-seen)))


; day 7
; we need to define a local bit-not, since (bit-not 123) returns -124 and not 65412 as
; needed for the problem
(require '[clojure.string :as str])
(def day-7-input "/Users/alandgraf/code/advent/day7.txt")
(def day-7-input-fixed "/Users/alandgraf/code/advent/day7-lx-hacked.txt")
(def day-7-sample "/Users/alandgraf/code/advent/day7-sample-data.txt")

(defn bit-not-unsigned
  "An unsigned 16-bit Integer bitwise not"
  [n]
  (- 65535 n))

(defn parse-int-if-possible
  "returns the integer of i if possible, otherwise returns i"
  [i]
  (try
    (Integer. i)
    (catch Exception e
      i)))

(defn parse-output-wire [s]
  (last (map str/trim (str/split s #"->"))))

(defn parse-input-wires [s]
  (first (map str/trim (str/split s #"->"))))

(defn single-values-to-keywords
  "Assumes value is an array, if only one elemenet it returns that element as a keyword, else returns the
  value"
  [value]
  (if (> (count value) 1)
    value
    (parse-int-if-possible (first value))))

(defn map-values
  "returns a map of the map (m) with the values mapped over the function (f)"
  [m f]
  (into {} (for [[k v] m] [k (f v)])))

(defn parse-circuit-data-into-map
  "returns map of circuit, usage (parse-circuit-data-into-map (str/split (slurp day-7-sample) #\"\\n\"))"
  [circuit-input]
  (reduce
    (fn [acc next-circuit]
      (let [output-wire (keyword (parse-output-wire next-circuit))
            input-wires-str (parse-input-wires next-circuit)
            input-wires (str/split input-wires-str #" ")]
        (assoc acc output-wire input-wires)))
    {}
    circuit-input)
  )


(defn build-circuit
  "returns a map of input's data
  Usage: (build-circuit \"day-7-data.txt\")
  {:i [\"NOT\" \"y\"], :h [\"NOT\" \"x\"], :g [\"y\" \"RSHIFT\" \"2\"], :f [\"x\" \"LSHIFT\" \"2\"], :e [\"x\" \"OR\" \"y\"], :d [\"x\" \"AND\" \"y\"], :y :456, :x :123}"
  [circuit-input]
  (map-values (parse-circuit-data-into-map (str/split (slurp circuit-input) #"\n")) single-values-to-keywords))

(defn handle-not [value-raw circuit callable]
  (let [value (parse-int-if-possible value-raw)]
    (if (number? value)
      (bit-not-unsigned value)
      (bit-not-unsigned (callable (keyword value) circuit)))))

(defn handle-ops [signal circuit callable]
  (case (count (signal circuit))
    2 (handle-not (last (signal circuit)) circuit callable)
    ; handle LSHIFT RSHIFT AND OR
    3 (let [[left-val-raw opcode right-val-raw] (signal circuit)
            left-val-parsed (parse-int-if-possible left-val-raw)
            right-val-parsed (parse-int-if-possible right-val-raw)
            left-val (if (number? left-val-parsed)
                       left-val-parsed
                       (callable (keyword left-val-parsed) circuit))
            right-val (if (number? right-val-parsed)
                        right-val-parsed
                        (callable (keyword right-val-parsed) circuit))]
        (case opcode
          "LSHIFT" (bit-shift-left left-val right-val)
          "RSHIFT" (bit-shift-right left-val right-val)
          "AND" (bit-and left-val right-val)
          "OR" (bit-or left-val right-val))
        )))

(def ^{:doc "returns the value of signal (keyword) within a circuit (map)\n  Usage: (find-signal :i sample-built-circuit)"}
  find-signal
  (memoize (fn
             [signal circuit]
             (if (integer? (signal circuit))  ; N.B. could also switch on the type here
               (signal circuit)
               (if (string? (signal circuit))  ; either a string, "lx", or array, ["x" "LSHIFT" "y"] or ["NOT" "x"]
                 (find-signal (keyword (signal circuit)) circuit)
                 (handle-ops signal circuit find-signal))))))

; Timing: the memozied version copletes in 0.055178 msecs, the non-memoized version never completed
; as it probably went into swap

; Data was loaded via: (def day-7-data (build-circuit day-7-input))
; For the solution to part B, I just ran (find-signal :a (assoc day-7-data :b (find-signal :a day-7-data)))


; Day 19
(def day-19-subs "/Users/alandgraf/code/advent/day19-substitutions.txt")
(def day-19-molecule "/Users/alandgraf/code/advent/day19-molecule.txt")
(use 'clojure.java.io)

(defn print-file-contents [filename]
  (with-open [rdr (reader filename)]
    (doseq [line (line-seq rdr)]
      (println line))) )