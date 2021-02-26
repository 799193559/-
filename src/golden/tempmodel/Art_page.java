package golden.tempmodel;

import java.util.List;

import golden.model.Article;

public class Art_page {
    private List<Article> article;
    private int total;
	public List<Article> getArticle() {
		return article;
	}
	public void setArticle(List<Article> article) {
		this.article = article;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}
