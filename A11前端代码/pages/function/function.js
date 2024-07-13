// pages/function/function.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    activeName: "1",
    functions: [
      {
        text: "动物识别",
        url: "../image/animal.png",
      },
      {
        text: "车辆识别",
        url: "../image/car.png",
      },
      {
        text: "货币识别",
        url: "../image/money.png",
      },
      {
        text: "菜品识别",
        url: "../image/food.png",
      },
      {
        text: "果蔬识别",
        url: "../image/fruit.png",
      },
      {
        text: "地标识别",
        url: "../image/landmark.png",
      },
      {
        text: "logo识别",
        url: "../image/logoide.png",
      },
      {
        text: "植物识别",
        url: "../image/plant.png",
      },
    ],
    functionId: "",
    drawFile: "",
    draws: [
      {
        text: "手写文字",
        url: "../image/handwrite.png",
      },
      {
        text: "手写文字(文本纠错)",
        url: "../image/handerror.png",
      },
      // {
      //   text: "通用文字",
      //   url: "",
      // },
      {
        text: "通用文字(文本纠错)",
        url: "../image/generictext.png",
      },
    ],
  },
  identifys(e) {
    let that = this;
    this.setData({ drawFile: e.detail.file });
    // console.log(event.detail.file.url);
    wx.navigateTo({
      url:
        "/pages/dishIdentify/dishIdentify?file=" +
        JSON.stringify(that.data.drawFile) +
        "&id=" +
        that.data.functionId,
    });
    console.log(e);
  },
  identical(e) {
    let that = this;
    console.log(e.detail.file);
    this.setData({
      file: e.detail.file.url,
    });
    wx.navigateTo({
      url: "/pages/identifys/identifys?file=" + JSON.stringify(that.data.file),
    });
  },
  getId(e) {
    // console.log(e);
    this.setData({
      functionId: e.currentTarget.dataset.index,
    });
  },
  draws(e) {
    let that = this;
    this.setData({ drawFile: e.detail.file });
    // console.log(event.detail.file.url);
    wx.navigateTo({
      url:
        "/pages/keys/keys?file=" +
        JSON.stringify(that.data.drawFile) +
        "&id=" +
        that.data.functionId,
    });
    console.log(e);
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onChange(event) {
    this.setData({
      activeName: event.detail,
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
  onLoad(options) {},

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
