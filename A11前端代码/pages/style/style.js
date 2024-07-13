// pages/style/style.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    styleInfo: [
      {
        text: "探索无限",
        url: "http://www.elysialove.xyz/wenxin/front-image/01.webp",
        flag: false,
      },
      {
        text: "古风",
        url: "http://www.elysialove.xyz/wenxin/front-image/02.webp",
        flag: false,
      },
      {
        text: "二次元",
        url: "http://www.elysialove.xyz/wenxin/front-image/03.webp",
        flag: false,
      },
      {
        text: "写实风格",
        url: "http://www.elysialove.xyz/wenxin/front-image/04.webp",
        flag: false,
      },
      {
        text: "浮世绘",
        url: "http://www.elysialove.xyz/wenxin/front-image/05.webp",
        flag: false,
      },
      {
        text: "low poly ",
        url: "http://www.elysialove.xyz/wenxin/front-image/06.webp",
        flag: false,
      },
      {
        text: "未来主义",
        url: "http://www.elysialove.xyz/wenxin/front-image/07.webp",
        flag: false,
      },
      {
        text: "像素风格",
        url: "http://www.elysialove.xyz/wenxin/front-image/08.webp",
        flag: false,
      },
      {
        text: "概念艺术",
        url: "http://www.elysialove.xyz/wenxin/front-image/09.webp",
        flag: false,
      },
      {
        text: "赛博朋克",
        url: "http://www.elysialove.xyz/wenxin/front-image/10.webp",
        flag: false,
      },
      {
        text: "洛丽塔风格",
        url: "http://www.elysialove.xyz/wenxin/front-image/11.webp",
        flag: false,
      },
      {
        text: "巴洛克风格",
        url: "http://www.elysialove.xyz/wenxin/front-image/12.webp",
        flag: false,
      },
      {
        text: "超现实主义",
        url: "http://www.elysialove.xyz/wenxin/front-image/13.webp",
        flag: false,
      },
      {
        text: "水彩画",
        url: "http://www.elysialove.xyz/wenxin/front-image/14.webp",
        flag: false,
      },
      {
        text: "蒸汽波艺术",
        url: "http://www.elysialove.xyz/wenxin/front-image/15.webp",
        flag: false,
      },
      {
        text: "油画",
        url: "http://www.elysialove.xyz/wenxin/front-image/16.webp",
        flag: false,
      },
      {
        text: "卡通画",
        url: "http://www.elysialove.xyz/wenxin/front-image/17.webp",
        flag: false,
      },
    ],
    id: "",
    keyWord: "",
    resolution: "",
  },

  /**
   * 生命周期函数--监听页面加载
   */
  getStyle(e) {
    var that = this;
    for (var i = 0; i < that.data.styleInfo.length; i++) {
      that.data.styleInfo[i].flag = false;
    }
    that.data.styleInfo[e.currentTarget.dataset.index].flag = true;
    that.setData({
      id: e.currentTarget.dataset.index,
    });
  },
  onLoad(options) {
    // console.log(options);
    this.setData({
      keyWord: options.keyWord,
      resolution: options.resolution,
    });
  },
  nextotherStyle() {
    var that = this;
    // console.log(that.data.styleInfo[that.data.id].text);
    wx.navigateTo({
      url:
        "/pages/otherstyle/otherstyle?keyWord=" +
        that.data.keyWord +
        "&resolution=" +
        that.data.resolution +
        "&style=" +
        that.data.styleInfo[that.data.id].text,
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
