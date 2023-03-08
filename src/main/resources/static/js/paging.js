function pagination(current, last) {
    let delta = 2,
    	range = [],
        rangeWithDots = [];
    for (let i = 1; i <= last; i++) {
        if (i == 1 || i == last || Math.abs(current - i) <= delta) {
            range.push(i);
        }
    }
    rangeWithDots.push(range[0]);
    for (let i = 1; i < range.length; i++) {
    	let distance = range[i] - range[i - 1];
    	if(distance == 2){
    		rangeWithDots.push(range[i] - 1);
    	}else if (distance > 2){
    		rangeWithDots.push("...");
    	}
    	rangeWithDots.push(range[i]);
    }
    return rangeWithDots;
}

function addParamToUrl(currentUrl, name, value){
    let url = new URL(currentUrl);
    url.searchParams.set(name, value);
    return url.href;
}


