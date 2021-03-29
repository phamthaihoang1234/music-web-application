package com.codegym.music.model;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Table(name = "songs")
public class Song implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "song_album",
    joinColumns = @JoinColumn(name = "song_id"),
    inverseJoinColumns = @JoinColumn(name = "album_id"))
    private Collection<Album> albums;

    @ManyToOne
    @JoinColumn(name = "singer_id")
    private Singer singer;

    @Column(name = "name",nullable = false)
    @NotEmpty
    private String name;

    @Column(name = "lyric",nullable = false,columnDefinition = "TEXT")
    private String lyric;

    @Basic(optional = false)
    private String image;

    @Column(name = "url",nullable = false)
    private String url;

    @Column(name = "status",nullable = false)
    private boolean status;

    @Column(name = "created_at", columnDefinition = "DATE")
    private LocalDate createAt;

    @Column
    private Integer views;

    @Column
    private Integer likeCount;

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }



    public Song() {
    }

    @Transient
    private MultipartFile imageData;

    @Transient
    private MultipartFile mp3Data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Collection<Album> albums) {
        this.albums = albums;
    }

    public Singer getSinger() {
        return singer;
    }

    public void setSinger(Singer singer) {
        this.singer = singer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate create_at) {
        this.createAt = create_at;
    }

    public MultipartFile getImageData() {
        return imageData;
    }

    public void setImageData(MultipartFile imageData) {
        this.imageData = imageData;
    }

    public MultipartFile getMp3Data() {
        return mp3Data;
    }

    public void setMp3Data(MultipartFile mp3Data) {
        this.mp3Data = mp3Data;
    }
}
