package patterns.templatemethod;

/**
 * Template Method Pattern Demo.
 */
class TemplateMethod {

    public static void main(String[] args){
        AbstractLetter letter = new FancyLetter();
        letter.createLetter(
                "\nHi!\n" +
                "I hope you're having a good day!\n" +
                "Bye!\n");
    }

    /**
     * Template Method Pattern for creating a letter
     * with a custom Header and Footer.
     */
    private static abstract class AbstractLetter {

        abstract String addHeader();

        abstract String addFooter();

        abstract void deliver(String letter);

        private void createLetter(String content){
            StringBuilder sb = new StringBuilder();
            sb.append(this.addHeader());
            sb.append(content);
            sb.append(this.addFooter());

            this.deliver(sb.toString());
        }

    }

    /**
     * Add a fancy header a footer to a letter.
     */
    private static class FancyLetter extends AbstractLetter {

        @Override
        String addHeader() {
            return this.doCreateDecoration();
        }

        @Override
        String addFooter() {
            return this.doCreateDecoration();
        }

        private String doCreateDecoration(){
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<10; i++){
                sb.append("*~*");
            }

            return sb.toString();
        }

        @Override
        void deliver(String letter) {
            System.out.println("Sending the letter...");
            System.out.println(letter);
        }
    }
}
