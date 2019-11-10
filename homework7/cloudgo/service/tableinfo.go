package service

import (
	"net/http"

	"github.com/unrolled/render"
)

func tableinfoHandler(formatter *render.Render) http.HandlerFunc {

	return func(w http.ResponseWriter, req *http.Request) {
		req.ParseForm()
		formatter.HTML(w, http.StatusOK, "login", struct {
			Username  string
			Password string
			Email string
		}{Username: req.Form["username"][0], Password: req.Form["password"][0], Email: req.Form["email"][0]})
	}
}