// pages/index/index.js
const app = getApp();
Page({
  /**
   * 页面的初始数据
   */
  data: {
    file: "",
    baikeNum: 1,
    fileInfo: [],
    drawFile: "",
    drawInfo: [],
  },

  identifys(event) {
    let that = this;
    console.log(event.detail.file);
    this.setData({
      file: event.detail.file.url,
    });
    wx.navigateTo({
      url: "/pages/identifys/identifys?file=" + JSON.stringify(that.data.file),
    });
  },
  identify(event) {
    let that = this;
    console.log(event.detail.file);
    this.setData({
      file: event.detail.file,
    });
    wx.navigateTo({
      url:
        "/pages/dishIdentify/dishIdentify?file=" +
        JSON.stringify(that.data.file) +
        "&id=3",
    });
  },
  draws(event) {
    let that = this;
    this.setData({ drawFile: event.detail.file });
    wx.navigateTo({
      url:
        "/pages/keys/keys?file=" + JSON.stringify(that.data.drawFile) + "&id=2",
    });
  },
  toTele() {
    wx.navigateTo({
      url: "/pages/teletextConverter/teletextConverter",
    });
  },
  toclear(e) {
    let that = this;
    console.log(e.detail.file);
    this.setData({
      file: e.detail.file.url,
    });
    wx.navigateTo({
      url: "/pages/clear/clear?file=" + JSON.stringify(that.data.file),
    });
  },
  toSenior() {
    wx.navigateTo({
      url: "/pages/seniordraw/seniordraw",
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    wx.request({
      url: "http://www.elysialove.xyz:7701/user/info/getUserInfo",
      data: {},
      header: {
        "content-type": "application/json",
        Authorization: wx.getStorageSync("token"),
        RefreshToken: wx.getStorageSync("refresh_token"),
      },
      method: "GET",
      dataType: "json",
      responseType: "text",
      success: (result) => {
        console.log(result.data);
        let code = result.data.code;
        if (code == 200 && result.data.data.userIcon != null) {
          app.globalData.userInfo = result.data.data;
        } else {
        }
      },
      fail: () => {},
      complete: () => {},
    });
  },
  tonews() {
    wx.navigateTo({
      url: "/pages/newone/newone",
    });
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {},

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    wx.hideHomeButton(); //隐藏home/返回主页按钮
  },

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
