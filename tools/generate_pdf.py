from reportlab.lib.pagesizes import A4
from reportlab.pdfgen import canvas
from reportlab.lib.units import mm

import sys

INPUT = r"c:\Users\USER\sara 2\docs\SARA_speaker_cards_and_scripts.txt"
OUTPUT = r"c:\Users\USER\sara 2\docs\SARA_speaker_cards_and_scripts.pdf"

PAGE_WIDTH, PAGE_HEIGHT = A4
MARGIN = 20 * mm

def draw_text_file(input_path, output_path):
    c = canvas.Canvas(output_path, pagesize=A4)
    text_obj = c.beginText()
    text_obj.setTextOrigin(MARGIN, PAGE_HEIGHT - MARGIN)
    text_obj.setFont("Helvetica", 11)
    line_height = 13

    with open(input_path, 'r', encoding='utf-8') as f:
        for raw_line in f:
            line = raw_line.rstrip('\n')
            # wrap long lines
            max_chars = 95
            while len(line) > max_chars:
                chunk = line[:max_chars]
                text_obj.textLine(chunk)
                line = line[max_chars:]
                if text_obj.getY() < MARGIN + line_height:
                    c.drawText(text_obj)
                    c.showPage()
                    text_obj = c.beginText()
                    text_obj.setTextOrigin(MARGIN, PAGE_HEIGHT - MARGIN)
                    text_obj.setFont("Helvetica", 11)
            text_obj.textLine(line)
            if text_obj.getY() < MARGIN + line_height:
                c.drawText(text_obj)
                c.showPage()
                text_obj = c.beginText()
                text_obj.setTextOrigin(MARGIN, PAGE_HEIGHT - MARGIN)
                text_obj.setFont("Helvetica", 11)

    c.drawText(text_obj)
    c.save()

if __name__ == '__main__':
    draw_text_file(INPUT, OUTPUT)
    print(f"Wrote PDF: {OUTPUT}")
