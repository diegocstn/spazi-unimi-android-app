function SVGFloor(){
    this.svgContainer = document.getElementById('svg');
}

SVGFloor.prototype.init = function(svgURL,fn){
    var xhr     = new XMLHttpRequest();
    var that    = this;
    xhr.open("GET",svgURL);
    xhr.onload = function(e){
        if( xhr.status === 200 ){
            that.svgContainer.innerHTML = xhr.responseText;
            if( fn ){
                fn.apply(that);
            }
        }
    }

    xhr.send();
}
SVGFloor.prototype.selectRoom = function(r_id,color){
    var polylines   = document.querySelectorAll('polyline');
    var room        = document.getElementById(r_id).querySelector('polyline');
    for( var i=0;i<polylines.length;i++ ){
        polylines[i].style.opacity = 0.3;
    }
    room.style.opacity = 1;
    room.style.fill = color;
}
SVGFloor.prototype.initAndSelectRoom = function(svgURL,r_id,color){
    var that = this;
    this.init(svgURL,function(){
        that.selectRoom(r_id,color);
    });
}

var svgFloor = new SVGFloor();