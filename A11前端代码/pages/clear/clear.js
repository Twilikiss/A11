// pages/clear/clear.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    file: "",
    imgUrl: "",
    flag: false,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  save() {
    //获取文件管理器对象
    const fs = wx.getFileSystemManager();
    //文件保存路径
    const Imgpath = wx.env.USER_DATA_PATH + "/clearWrite" + ".png";
    //base64图片文件
    let imageSrc = this.data.imgUrl;

    //写入本地文件
    fs.writeFile({
      filePath: Imgpath,
      data: imageSrc,
      encoding: "base64",
      success(res) {
        console.log(res);

        //保存到手机相册
        wx.saveImageToPhotosAlbum({
          filePath: Imgpath,
          success(res) {
            console.log(res);
            wx.showToast({
              title: "保存成功",
              icon: "success",
            });
          },
          fail: function (err) {
            console.log("失败了");
            console.log(err);
          },
        });
      },
    });
  },
  onLoad(options) {
    console.log(JSON.parse(options.file));
    this.setData({ file: JSON.parse(options.file) });
    var that = this;
    // wx.showToast({
    //   title: "正在识别中",
    //   icon: "loading",

    //   duration: 4000, //持续的时间
    // });
    wx.showToast({
      title: "去除痕迹中",
      icon: "loading",
      duration: 4000,
    });
    that.clear();
    setTimeout(() => {
      that.setData({ flag: true });
    }, 4100);
  },
  clear() {
    var that = this;
    wx.uploadFile({
      filePath: that.data.file,
      name: "multipartFile",
      url: "http://www.elysialove.xyz:7701/api/img/ocr/remove_handwriting?type=image",
      header: {
        "Content-Type": "application/json",
        // accept: "application/json",
        Authorization: wx.getStorageSync("token"),
        RefreshToken: wx.getStorageSync("refresh_token"),
      },
      formData: {},
      success(res) {
        console.log(JSON.parse(res.data));
        let data = JSON.parse(res.data);
        let code = data.code;
        if (code > 400) {
          let arr = data.message.split(";");
          let token = arr[0].substring(13);
          let refresh_token = arr[1].substring(14);
          wx.setStorage({
            key: "token",
            data: token,
          });
          wx.setStorage({
            key: "refresh_token",
            data: refresh_token,
          });
          that.clear();
        } else if (code > 200 && code < 300) {
          let message = data.message;
          wx.showToast({
            title: message,
            icon: "error",
            duration: 2000, //持续的时间
          });
        } else {
          that.setData({ imgUrl: data.data.results.image_processed });
        }
      },
    });
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {},

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {},

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {},

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {},

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {},

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {},

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {},
});
