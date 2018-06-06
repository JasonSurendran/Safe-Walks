var streetArr = [];
var count = [3];


	function dataGrab(){
		var rootRef = firebase.database().ref().child("datapoints");

		function snapshotToArray(snapshot) {
			var returnArr = [];

			snapshot.forEach(function(childSnapshot) {
				var item = childSnapshot.val();
				item.key = childSnapshot.key;

				returnArr.push(item);
			});

			return returnArr;
		}

		rootRef.on("value", function(snapshot) {
			var rawArr = snapshotToArray(snapshot);
			var i = 0;
			var str;

			count[0] = 0;
			count[1] = 0;
			count[2] = 0;
			while(i < rawArr.length){
				str = rawArr[i];
				streetArr[i] = str.substring(39);
				if (streetArr[i]=="Jinan and Huanghe"){
        	count[0]++;
    		}
    		if (streetArr[i]=="Zhongsan and King Street"){
        	count[1]++;
    		}
    		if (streetArr[i]=="Hanxing and Daliang"){
        	count[2]++;
    		}
				i++;
			}
			console.log(streetArr);
			console.log(count);
		});
}
dataGrab();
console.log(streetArr);
console.log(count);
