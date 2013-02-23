//缩放图片
function shrinkImage(imgObj, width, height) {
	var image = new Image();
	image.src = imgObj.src;
	if (image.width > 0 && image.height > 0) {
		var rate = (width / image.width < height / image.height) ? width
				/ image.width : height / image.height;
		if (rate <= 1) {
			imgObj.width = image.width * rate;
			imgObj.height = image.height * rate;
		} else {
			imgObj.width = image.width;
			imgObj.height = image.height;
		}
	}
}