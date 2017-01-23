package models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by toussaintpeterson on 1/7/16.
 */
public class Game implements Parcelable{
    private String gameName;
    private String wager;
    private String officialNumbers;
    private String userNumbers;
    private String userCode;
    private String gameType;
    private String gamePlay;
    private int availableNumbers;
    private String categoryName;
    private String subCategory;
    private String isQuickPick;
    private String isFeatured;
    private String maxNumber;
    private String gameDescription;
    private String logoText;
    private String extendedDescription;
    private String iconSet;
    private String filterString;
    private String gameId;
    private String backURL;
    private String logoURL;
    private String maxValue;
    private String numbersRequired;
    private int bubbleImage;
    private String iconUrl;
    private String teaserUrl;
    private String bannerUrl;
    private String topPrize;
    private String maxWin;
    private String thumbImage;
    private int templateId;
    private String savedNumbers;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getWager() {
        return wager;
    }

    public void setWager(String wager) {
        this.wager = wager;
    }

    public String getOfficialNumbers() {
        return officialNumbers;
    }

    public void setOfficialNumbers(String officialNumbers) {
        this.officialNumbers = officialNumbers;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserNumbers() {
        return userNumbers;
    }


    public String getIsQuickPick() {
        return isQuickPick;
    }

    public void setIsQuickPick(String isQuickPick) {
        this.isQuickPick = isQuickPick;
    }

    public void setUserNumbers(String userNumbers) {
        this.userNumbers = userNumbers;
    }

    public String getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(String isFeatured) {
        this.isFeatured = isFeatured;
    }

    public int getAvailableNumbers() {
        return availableNumbers;
    }

    public void setAvailableNumbers(int availableNumbers) {
        this.availableNumbers = availableNumbers;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getGamePlay() {
        return gamePlay;
    }

    public void setGamePlay(String gamePlay) {
        this.gamePlay = gamePlay;
    }

    public String getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(String maxNumber) {
        this.maxNumber = maxNumber;
    }

    public String getGameDescription() {
        return gameDescription;
    }

    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
    }

    public String getExtendedDescription() {
        return extendedDescription;
    }

    public void setExtendedDescription(String extendedDescription) {
        this.extendedDescription = extendedDescription;
    }

    public String getLogoText() {
        return logoText;
    }

    public void setLogoText(String logoText) {
        this.logoText = logoText;
    }

    public String getFilterString() {
        return filterString;
    }

    public void setFilterString(String filterString) {
        this.filterString = filterString;
    }

    public String getIconSet() {
        return iconSet;
    }

    public void setIconSet(String iconSet) {
        this.iconSet = iconSet;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getBackURL() {
        return backURL;
    }

    public void setBackURL(String backURL) {
        this.backURL = backURL;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getNumbersRequired() {
        return numbersRequired;
    }

    public void setNumbersRequired(String numbersRequired) {
        this.numbersRequired = numbersRequired;
    }

    public int getBubbleImage() {
        return bubbleImage;
    }

    public void setBubbleImage(int bubbleImage) {
        this.bubbleImage = bubbleImage;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTeaserUrl() {
        return teaserUrl;
    }

    public void setTeaserUrl(String teaserUrl) {
        this.teaserUrl = teaserUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getMaxWin() {
        return maxWin;
    }

    public void setMaxWin(String maxWin) {
        this.maxWin = maxWin;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public String getTopPrize() {
        return topPrize;
    }

    public void setTopPrize(String topPrize) {
        this.topPrize = topPrize;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getSavedNumbers() {
        return savedNumbers;
    }

    public void setSavedNumbers(String savedNumbers) {
        this.savedNumbers = savedNumbers;
    }

    private void readFromParcel(Parcel in) {
        this.gameName = in.readString();
        this.wager = in.readString();
        this.isQuickPick = in.readString();
        this.isFeatured = in.readString();
        this.categoryName = in.readString();
        this.subCategory = in.readString();
        this.gameType = in.readString();
        this.gamePlay = in.readString();
        this.maxNumber = in.readString();
        this.gameDescription = in.readString();
        this.extendedDescription = in.readString();
        this.iconSet = in.readString();
        this.logoText = in.readString();
        this.filterString = in.readString();
        this.gameId = in.readString();
        this.logoURL = in.readString();
        this.backURL = in.readString();
        this.maxValue = in.readString();
        this.bubbleImage = in.readInt();
        this.iconUrl = in.readString();
        this.teaserUrl = in.readString();
        this.bannerUrl = in.readString();
        this.maxWin = in.readString();
        this.thumbImage = in.readString();
        this.topPrize = in.readString();
        this.templateId = in.readInt();
        this.savedNumbers = in.readString();
    }

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {

        @Override
        public Game createFromParcel(Parcel source){
            Game game = new Game();
            game.gameName = source.readString();
            game.wager = source.readString();
            game.isQuickPick = source.readString();
            game.isFeatured = source.readString();
            game.categoryName = source.readString();
            game.subCategory = source.readString();
            game.gameType = source.readString();
            game.gamePlay = source.readString();
            game.maxNumber = source.readString();
            game.gameDescription = source.readString();
            game.extendedDescription = source.readString();
            game.iconSet = source.readString();
            game.logoText = source.readString();
            game.filterString = source.readString();
            game.gameId = source.readString();
            game.logoURL = source.readString();
            game.backURL = source.readString();
            game.maxValue = source.readString();
            game.bubbleImage = source.readInt();
            game.iconUrl = source.readString();
            game.teaserUrl = source.readString();
            game.bannerUrl = source.readString();
            game.maxWin = source.readString();
            game.thumbImage = source.readString();
            game.topPrize = source.readString();
            game.templateId = source.readInt();
            game.savedNumbers = source.readString();

            return game;
        }

        @Override
        public Game[] newArray(int i) {
            return new Game[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(gameName);
        parcel.writeString(wager);
        parcel.writeString(isQuickPick);
        parcel.writeString(isFeatured);
        parcel.writeString(categoryName);
        parcel.writeString(subCategory);
        parcel.writeString(gameType);
        parcel.writeString(gameType);
        parcel.writeString(maxNumber);
        parcel.writeString(gameDescription);
        parcel.writeString(extendedDescription);
        parcel.writeString(iconSet);
        parcel.writeString(logoText);
        parcel.writeString(filterString);
        parcel.writeString(gameId);
        parcel.writeString(logoURL);
        parcel.writeString(backURL);
        parcel.writeString(maxValue);
        parcel.writeInt(bubbleImage);
        parcel.writeString(iconUrl);
        parcel.writeString(teaserUrl);
        parcel.writeString(bannerUrl);
        parcel.writeString(maxWin);
        parcel.writeString(thumbImage);
        parcel.writeString(topPrize);
        parcel.writeInt(templateId);
        parcel.writeString(savedNumbers);
    }
}
