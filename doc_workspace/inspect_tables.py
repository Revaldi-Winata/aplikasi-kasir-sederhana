import docx
doc = docx.Document(r"E:\File Kuliah\Semester 6\Pemrograman II\Praktek\TokoBerkahJaya\doc_workspace\TokoBerkahJaya_Dokumentasi.docx")
for i, table in enumerate(doc.tables):
    print(f"Table {i}:")
    for row in table.rows:
        print([cell.text.replace('\n', ' ').strip() for cell in row.cells][:4])
