lint:
	lein kibit
	lein eastwood

check: lint
	lein test
