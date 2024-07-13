// pages/otherstyle/otherstyle.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    otherStyle: [
      "cg感",
      "厚涂版绘",
      "精致面容",
      "穆夏",
      "机械感",
      "宫崎骏动画",
      "烟雾缭绕",
      "皮克斯动画",
      "拟人化",
      "剪纸叠加",
      "色彩斑斓",
      "城市印象",
      "上半身立绘",
      "电影质感",
      "扁平化",
      "简约logo",
      "细节清晰",
      "毛发细致",
    ],
    arr: [],
    other: [],
    keyword: "",
    placeSelected: "",
    resolution: "",
    style: "",
    taskId: "",
  },
  getOther(e) {
    let index = e.currentTarget.dataset.index;
    var that = this;
    if (that.data.other.length < 5) {
      if (that.data.other.length == 0) {
        that.data.arr.push(that.data.otherStyle[index]);
      } else {
        var flag = false;
        for (var i = 0; i < that.data.arr.length; i++) {
          if (that.data.otherStyle[index] == that.data.arr[i]) {
            flag = true;
            break;
          }
        }
        if (flag == true) {
          wx.showToast({
            title: "已经选过了",
            icon: "error",
            duration: 1000, //持续的时间
          });
        } else {
          that.data.arr.push(that.data.otherStyle[index]);
        }
      }
      that.setData({
        other: that.data.arr,
      });
    } else {
      wx.showToast({
        title: "最多选五个！",
        icon: "error",
        duration: 2000, //持续的时间
      });
    }
  },
  delete(e) {
    let index = e.currentTarget.dataset.index;
    this.data.arr.splice(index, 1);
    this.setData({
      other: this.data.arr,
    });
  },
  gettaskId() {
    // api/img/ai_painting/base/text2image
    var that = this;
    wx.showModal({
      title: "提示",
      content: "是否确认消息",
      success: function (res) {
        if (res.confirm) {
          wx.request({
            url: `http://www.elysialove.xyz:7701/api/ai_painting/base/text2image`,
            data: {
              keyword: that.data.keyword,
              num: 1,
              otherStyle: that.data.other,
              resolution: that.data.resolution,
              style: that.data.style,
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
              console.log(result.data.data.data.taskId);
              let taskId = result.data.data.data.taskId;
              that.setData({ taskId: taskId });
              wx.showToast({
                title: "点击绘图",
                icon: "none",
                image: "",
                duration: 1500,
                mask: false,
                success: (result) => {},
                fail: () => {},
                complete: () => {},
              });
            },
            fail: () => {},
            complete: () => {},
          });
        } else {
        }
      },
    });

    // var taskId = 16505062;
    // wx.navigateTo({
    //   url: "/pages/draws/draws?taskId=" + taskId,
    // });
  },
  nextDraws() {
    wx.navigateTo({
      url: "/pages/draws/draws?taskId=" + this.data.taskId,
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.setData({
      keyword: options.keyWord,
      resolution: options.resolution,
      style: options.style,
    });
    // console.log(options);
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
