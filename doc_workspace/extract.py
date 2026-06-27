import xml.etree.ElementTree as ET
import sys
tree = ET.parse(sys.argv[1])
root = tree.getroot()
ns = {'w': 'http://schemas.openxmlformats.org/wordprocessingml/2006/main'}
text = []
for p in root.iter('{%s}p' % ns['w']):
    p_text = []
    for t in p.iter('{%s}t' % ns['w']):
        if t.text:
            p_text.append(t.text)
    if p_text:
        text.append(''.join(p_text))
print('\n'.join(text))
