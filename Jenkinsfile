def date = new Date()

pipeline {
	agent any
	stages {
		stage ('HolaMundo') {
			steps {
				echo "Hoy es día " + date.format('dd-MM-yyyy')
				echo "Chocolate Blanco		1000"
				echo "Chocolate Negro		1500"
				echo "Chocolate con almendras		1200"
				echo "Chocolate con castañas de caju		1300"
				echo "Chocolate en rama		100"
				echo "Chocolate con 70% de cacao		1500"
			}
		}
	}
}
