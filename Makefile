all:
	cd tests && ant clean && ant debug install && adb shell "am start -n hongbosb.rollingball/.MainActivity"
run:
	adb shell "input keyevent 3 && am kill hongbosb.rollingball" && adb shell "am start -n hongbosb.rollingball/.MainActivity"
