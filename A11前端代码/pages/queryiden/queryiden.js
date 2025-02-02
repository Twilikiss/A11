// pages/queryiden/queryiden.js
Page({
  /**
   * 页面的初始数据
   */
  data: {},

  /**
   * 生命周期函数--监听页面加载
   */
  onChange(event) {
    // console.log(event.detail.index);
    wx.showToast({
      title: `切换到${event.detail.title}`,
      icon: "none",
    });
    if (event.detail.index == 0) {
      this.getIden();
    } else if (event.detail.index == 1) {
      this.getEnh();
    } else {
      this.getOcr();
    }
  },
  onLoad(options) {
    this.getIden();
  },
  get(url) {
    var that = this;
    wx.request({
      url: url,
      data: {},
      header: {
        "Content-Type": "application/json",
        // accept: "application/json",
        Authorization: wx.getStorageSync("token"),
        RefreshToken: wx.getStorageSync("refresh_token"),
      },
      method: "GET",
      dataType: "json",
      responseType: "text",
      success: (result) => {
        console.log(result);
        let data = result.data;
        let code = data.code;
        if (code > 400) {
          let arr = data.message.split(";");
          let token = arr[0].substring(13);
          let refresh_token = arr[1].substring(14);
          console.log(token);
          console.log(refresh_token);
          wx.setStorage({
            key: "token",
            data: token,
          });
          wx.setStorage({
            key: "refresh_token",
            data: refresh_token,
          });
          that.getIden();
        } else if (code == 208) {
          wx.showToast({
            title: "请重新登录",
            icon: "error",
            duration: 2000, //持续的时间
          });
          setTimeout(() => {
            wx.navigateTo({
              url: "/pages/login/login",
            });
          }, 2100);
          // console.log(res);
        } else {
          that.setData({
            info: data.data,
          });
        }
      },
      fail: () => {},
      complete: () => {},
    });
  },
  getIden() {
    this.get(
      "http://www.elysialove.xyz:7701/user/info/history/image_classify/all"
    );
  },
  getEnh() {
    this.get(
      "http://www.elysialove.xyz:7701/user/info/history/image_process/all"
    );
  },
  getOcr() {
    this.get("http://www.elysialove.xyz:7701/user/info/history/ocr/all");
  },
  // /
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
