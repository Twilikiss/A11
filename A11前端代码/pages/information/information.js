// pages/information/information.js
const app = getApp();

Page({
  /**
   * 页面的初始数据
   */
  data: {
    fileList: "../image/avatar.png",
    percentage: 0,
    phone: "",
  },
  afterRead(event) {
    console.log(event.detail.file);
    this.setData({
      fileList: event.detail.file.url,
    });
    var that = this;
    // 上传数据库
    wx.uploadFile({
      filePath: that.data.fileList,
      name: "multipartFile",
      url: `http://www.elysialove.xyz:7701/user/info/uploadIcon`,
      header: {
        "Content-Type": "application/json",
        // accept: "application/json",
        Authorization: wx.getStorageSync("token"),
        RefreshToken: wx.getStorageSync("refresh_token"),
      },
      formData: {},
      success(res) {
        let data = JSON.parse(res.data);
        let code = data.code;
        if (code == 200) {
          app.globalData.userInfo.avatarUrl = data.data;
          that.setData({ fileList: data.data });
        } else {
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
        }
      },
    });
  },
  onChange(event) {
    this.setData({
      radio: event.detail,
    });
  },
  next() {
    // app.globalData.userInfo.avatarUrl = this.data.fileList;
    // console.log(app.globalData.userInfo.sex);
    var that = this;
    wx.showModal({
      title: "提示",
      content: "是否确认消息",
      success: function (res) {
        if (res.confirm) {
          that.completeInfo();
          wx.showToast({
            title: "登录成功",
            icon: "loading",
            duration: 2000, //持续的时间
          });
          setTimeout(() => {
            wx.switchTab({
              url: "/pages/index/index",
            });
          }, 2100);
        } else {
        }
      },
    });
  },
  completeInfo() {
    let gender = app.globalData.userInfo.gender;
    let nickName = app.globalData.userInfo.nickName;
    var that = this;
    wx.request({
      url: "http://www.elysialove.xyz:7701/user/info/completeInfo",
      data: {
        gender,
        nickName,
      },
      header: {
        "content-type": "application/json",
        Authorization: wx.getStorageSync("token"),
        RefreshToken: wx.getStorageSync("refresh_token"),
      },
      method: "POST",
      dataType: "json",
      responseType: "text",
      success: (result) => {
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
          that.completeInfo();
        } else if (code > 200 && code < 300) {
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
        } else {
          console.log(result);
        }
      },
      fail: () => {},
      complete: () => {},
    });
  },
  getPhone(e) {
    console.log(e.detail.value);
    this.setData({ phone: e.detail.value });
  },

  /**
   * 生命周期函数--监听页面加载
   */
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
