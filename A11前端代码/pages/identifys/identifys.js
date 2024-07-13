// pages/identifys/identifys.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    fileInfo: [],
    flag: false,
    file: "",
    url: "",
  },
  out() {
    wx.navigateTo({
      url:
        "/pages/out/out?baikeUrl=" + this.data.fileInfo[0].baike_info.baike_url,
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    console.log(JSON.parse(options.file));
    this.setData({ file: JSON.parse(options.file) });
    var that = this;
    wx.showToast({
      title: "正在识别中",
      icon: "loading",
      duration: 4000, //持续的时间
    });
    that.identify();
    setTimeout(() => {
      that.setData({ flag: true });
    }, 4100);

  },
  identify() {
    var that = this;
    wx.uploadFile({
      filePath: that.data.file,
      name: "multipartFile",
      url: `http://www.elysialove.xyz:7701/api/img/image_classify/universal?baikeNum=1`,
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
          that.identify();
        } else if (code > 200 && code < 300) {
          let message = data.message;
          wx.showToast({
            title: message,
            icon: "error",
            duration: 2000, //持续的时间
          });
        } else {
          that.setData({
            fileInfo: data.data.results.result,
            url: data.data.url,
          });
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
