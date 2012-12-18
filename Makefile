all:
	ant debug install&&adb shell "am start -n hongbosb.rollingball/.MainActivity"
