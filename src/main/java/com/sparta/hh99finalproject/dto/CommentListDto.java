package com.sparta.hh99finalproject.dto;

import com.sparta.hh99finalproject.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentListDto {

    private List<Comment> commentList;
}
