# MITUBA
gradleで管理するサーバー

## Usage

### mode

第一引数で以下のモードを選択してください

- search
検索のみを行い，類似度の頻度と検索時間を出力する

	- input
	- birthmark
	- maxcore
	- portnum
	
		- ex... java -jar MITUBA.jar search -i file -b 2gram -c 20 -p 8982

- compare
検索と比較を行う

	- input
	- birthmark
	- maxcore
	- portnum
	
		- ex... java -jar MITUBA.jar compare -i file -b 2gram -c 20 -p 8982


- eachCompare
単一バースマークと全バースマークを比較する

	- input
	- birthmark
	- dir
	
		- ex... java -jar MITUBA.jar eachCompare -i file -b 2gram -dir ./data
		
		
		

